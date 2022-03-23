/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.
		Set<String> processedBeans = new HashSet<>();

		// 1. 判断beanFactory是否是BeanDefinitionRegistry接口的实现类（true）
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			// 1.1 存放普通的BeanFactoryPostProcessor
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
			// 1.2 存放BeanDefinitionRegistryPostProcessor类型的BeanFactoryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			// 1.3 遍历参数中的beanFactoryPostProcessor（默认为空）
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				// 判断postProcessor是普通的工厂后处理器，还是BeanDefinitionPostProcessor类型的
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					// 1.3.1 首先执行BeanDefinitionRegistryPostProcessor中的方法postProcessBeanDefinitionRegistry
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					// 1.3.1 然后再讲registryProcessor存放起来(方便后续执行方法postProcessBeanFactory)
					registryProcessors.add(registryProcessor);
				}
				else {
					// 1.3.1 如果是普通的工厂后处理器，那就只实现了一个接口 BeanProcessPostProcessor
					// 存放到普通工厂后处理器的集合中
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			// 1.4 用户保存当前需要执行的BeanDefinitionRegistryPostProcessor(每处理完一批，会阶段性清空一批)
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			// 1.5 处理beanFactory中，及实现BeanDefinitionRegistryPostProcessor，有实现PriorityOrdered的实现类
			// 1） 从beanFactory中，获取类型为BeanDefinitionRegistryPostProcessor的所有的bean
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				// 1.1） 找到这些类中实现接口BeanDefinitionRegistryProcessor的实现类中，同时又实现了接口PriorityOrdered的实现类
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					// 1.1.1) 添加到当前处理的BeanDefinitionRegistryPostProcessor集合中
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					// 1.1.2) 记录类型为BeanDefinitionRegistryPostProcessor的bean的名称(编码后续相同的bean重复被执行了)
					processedBeans.add(ppName);
				}
			}
			// 2) 根据PriorityOrdered或者Ordered接口进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 3) 添加到registryProcessors集合中，（方便后续执行方法 postProcessBeanFactory）
			registryProcessors.addAll(currentRegistryProcessors);
			// 4) 执行currentRegistryProcessors中，第一个阶段的方法 ： postProcessBeanDefinitionRegistry
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			// 5） 执行完成后，清空，准备下一轮
			currentRegistryProcessors.clear();

			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			// 1.6 处理beanFactory中，及实现 BeanDefinitionRegistryPostProcessor，又实现Ordered的实现类
			// 以下操作和1.5一致，唯一区别就是PriorityOrdered接口编程了Ordered接口
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			// 1.7 处理beanFactory中，实现接口BeanDefinitionRegistryPostProcessor的实现类
			// 同时，这些实现了不能包含前面已经处理了的，也就是实现了PriorityOrdered解耦和Ordered接口的那些实现类
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				// 知道postProcessName为空，reiterate保持为false，才退出循环
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			// 1.8 统一调用BeanDefinitionRegistryPostProcessor类型工厂后处理器postProcessBeanFactory方法
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			// 1.9 统一调用普通类型工厂后处理器的postProcessBeanFactory方法
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
			/*
			分析到这里，BeanDefinitionRegistryPostProcessor中的两个方法，算是全部都执行完了。从这里，我们就可以初步总结出一些东西了：

			（1）首先处理参数中的beanFactoryPostProcessors按两种类型处理，分别是实现了接口BeanDefinitionRegistryPostProcessor的BeanFactoryPostProcessor，
			以及没实现该接口普通BeanFactoryPostProcessor，它们分别存放在集合registryProcessors和regularPostProcessors中。

			（2）如果实现了接口BeanDefinitionRegistryPostProcessor，率先会调用BeanDefinitionRegistryPostProcessor中的方法postProcessBeanDefinitionRegistry来注册一些BeanDefinition。

			（3）参数中的beanFactoryPostProcessors处理完毕之后，接着处理容器beanFactory中的BeanFactoryPostProcessor，
			优先从容器beanFactory中获取实现了接口BeanDefinitionRegistryPostProcessor的类，并且按照以下三种类型来处理：


			分别是实现了接口PriorityOrdered、Ordered以及这两个接口都没有实现的无序的普通类，
			和前面一样这三种类型的类都是实现了接口BeanDefinitionRegistryPostProcessor的，优先执行BeanDefinitionRegistryPostProcessor中的方法postProcessBeanDefinitionRegistry，
			注册一些自定义的BeanDefinition。


			（4）最后会统一执行BeanDefinitionRegistryPostProcessor的父类，以及普通BeanFactoryPostProcessor类中的方法postProcessBeanFactory，
			   完成一些自定义的修改BeanDefinition操作。
			 */
		}

		else {
			// Invoke factory processors registered with the context instance.
			// 1.1 如果beanFactory不是接口BeanDefinitionRegistry的实现类
			// 那么就是我们前面看到的普通工厂后处理器，直接调用postProcessBeanFactory方法了
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}


		/*
		   以上环节，参数beanFactoryPostProcessors，以及容器beanFactory中，
		   所有类型为BeanDefinitionRegistryPostProcessor的bean全部都处理了
		   接下来处理beanFactory中纯粹只实现接口BeanFactoryPostProcessor的bean
		 */

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		// 2. 处理beanFactory中，实现接口BeanFactoryPostProcessor的实现类
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		// 2.1 存放实现了PriorityOrdered接口的BeanFactoryPostProcessor接口实现类
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// 2.2 存放实现了Ordered接口的BeanFactoryPostProcessor接口实现类名字
		List<String> orderedPostProcessorNames = new ArrayList<>();
		// 2.4 存放无需的BeanFactoryPostProcessor接口实现类名字
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
				// 直接空实现，因为processedBean中 ，记录了前面处理BeanDefinitionRegistryPostProcessor的bean的名字
				// 表示已经处理过了，这里就不再重复处理
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		// 2.4 根据PriorityOrdered排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		// 2.5 执行工厂后处理器中postProcessBeanFactory方法
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		// 2.6 根据刚才记录的实现Ordered接口的bean的名字，从beanFactory中获取对应的bean
		// 根据Ordered排序并执行postProcessBeanFactory方法
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		// 2.7 根据刚才记录的无需的bean的名字，从beanFactory中获取对应的bean
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 无序排序，直接执行postProcessBeanFactory方法
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		// 2.8 清楚元数据相关的缓存，后处理器可能已经修改了原始的一些元数据
		beanFactory.clearMetadataCache();
	}

	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		// 1. 获取容器beanFactory中，所有的接口BeanPostProcessor类型的实现类
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		/*
		  得到最终容器中bean后处理器的数量
		 容器中已经注册的bean后处理器的数量 + 即将要注册的后处理BeanPostProcessChecker + 容器中还没有注册的bean后处理数量
		 */
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
  		// 2. 记录那些bean没有资格被所有bean后处理器处理，记录相应的日志信息
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// Separate between BeanPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		// 3. 初始化一些集合
		// 存放实现了接口PriorityOrdered的bean后处理器 BeanPostProcessor
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		// 存放spring内部bean后处理器BeanPostProcessor
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
		// 存放实现了接口Ordered的bean后处理器 BeanPostProcessor
		List<String> orderedPostProcessorNames = new ArrayList<>();
		// 存放无须的bean后处理器BeanPostProcessor
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();

		// 4. 遍历所有后处理器 BeanPostProcessor的名称
		for (String ppName : postProcessorNames) {
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				// 添加实现了PriorityOrdered的bean后处理器的名称
				priorityOrderedPostProcessors.add(pp);

				// 是spring内部的BeanPostProcessor,和实例化注解bean的关系密切，如@Autowired
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				// 添加实现了接口Ordered的bean后处理器名称
				orderedPostProcessorNames.add(ppName);
			}
			else {
				// 添加无序的bean后处理器名称
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, register the BeanPostProcessors that implement PriorityOrdered.
		// 5. 排序并将实现了PriorityOrdered的bean后处理器，并注册到容器 beanFactory中
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		// Next, register the BeanPostProcessors that implement Ordered.
		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
		for (String ppName : orderedPostProcessorNames) {
			// 根据名称,从容器中获取相应的bean后处理器BeanPostProcessor
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				// 如果是实现接口MergedBeanDefinitionPostProcessor的后处理器，添加到集合internalPostProcessors中
				internalPostProcessors.add(pp);
			}
		}
		// 6. 排序并将实现了接口Ordered的bean后处理器，注册到容器beanFactory中
		sortPostProcessors(orderedPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		// 7. 将无序普通的bean后处理器，注册到容器beanFactory中
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// Finally, re-register all internal BeanPostProcessors.
		// 8. 最后，将spring容器内部的BeanPostProcessor，注册到bean后处理器链的尾部
		sortPostProcessors(internalPostProcessors, beanFactory);
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		// Nothing to sort?
		if (postProcessors.size() <= 1) {
			return;
		}
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	/**
	 * Register the given BeanPostProcessor beans.
	 */
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
