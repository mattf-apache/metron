/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.metron.common.bolt;


import java.lang.invoke.MethodHandles;
import org.apache.metron.common.configuration.profiler.ProfilerConfig;
import org.apache.metron.common.configuration.profiler.ProfilerConfigurations;
import org.apache.metron.common.zookeeper.configurations.ConfigurationsUpdater;
import org.apache.metron.common.zookeeper.configurations.ProfilerUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bolt used in the Profiler topology that is configured with values stored in Zookeeper.
 */
public abstract class ConfiguredProfilerBolt extends ConfiguredBolt<ProfilerConfigurations> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public ConfiguredProfilerBolt(String zookeeperUrl) {
    super(zookeeperUrl);
  }

  protected ProfilerConfig getProfilerConfig() {
    return getConfigurations().getProfilerConfig();
  }

  @Override
  protected ConfigurationsUpdater<ProfilerConfigurations> createUpdater() {
    return new ProfilerUpdater(this, this::getConfigurations);
  }

}
