/*
 * Copyright 2020 the original author or authors.
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
package org.springframework.boot.autoconfigure.data.jpa;

import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.jpa.repository.support.JpaEvaluationContextExtension;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.graalvm.extension.NativeImageConfiguration;
import org.springframework.graalvm.extension.NativeImageHint;
import org.springframework.graalvm.extension.ProxyInfo;
import org.springframework.graalvm.extension.ResourcesInfo;
import org.springframework.graalvm.extension.TypeInfo;
import org.springframework.graalvm.type.AccessBits;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

@NativeImageHint(trigger = JpaRepositoriesAutoConfiguration.class,
		resourcesInfos = {
				@ResourcesInfo(patterns = "META-INF/jpa-named-queries.properties"),
				@ResourcesInfo(patterns="org.hibernate.validator.ValidationMessages",isBundle = true)
		},
		typeInfos = {
				@TypeInfo(types = {
						SharedEntityManagerCreator.class, // TODO is this one in the right place?
						JpaRepositoryFactoryBean.class,
						JpaEvaluationContextExtension.class,
						JpaQueryMethodFactory.class
				}, typeNames = {
						"org.springframework.data.jpa.repository.config.JpaMetamodelMappingContextFactoryBean",
						"org.springframework.data.jpa.util.JpaMetamodelCacheCleanup"
				}, access = AccessBits.CLASS | AccessBits.DECLARED_METHODS | AccessBits.DECLARED_CONSTRUCTORS | AccessBits.RESOURCE)
		},
		proxyInfos = {
				@ProxyInfo(typeNames = {
						"org.springframework.data.jpa.repository.support.CrudMethodMetadata", "org.springframework.aop.SpringProxy", "org.springframework.aop.framework.Advised", "org.springframework.core.DecoratingProxy"
				})
		}
)
// TODO Why can't I make this conditional on JpaReposAutoConfig above? The vanilla-orm sample needs this but JpaRepositoriesAutoConfiguration is not active in that sample
//@ConfigurationHint(typeInfos= {@TypeInfo(types= {PersistenceAnnotationBeanPostProcessor.class})}) // temporarily moved this to be HibernateJpaConfiguration dependant
public class JpaRepositoriesHints implements NativeImageConfiguration {

}
