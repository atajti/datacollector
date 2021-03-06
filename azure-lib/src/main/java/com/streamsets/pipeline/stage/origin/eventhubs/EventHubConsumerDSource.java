/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.stage.origin.eventhubs;

import com.streamsets.pipeline.api.ConfigDefBean;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.ExecutionMode;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.HideConfigs;
import com.streamsets.pipeline.api.PushSource;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.configurablestage.DPushSource;
import com.streamsets.pipeline.lib.eventhubs.EventHubConfigBean;
import com.streamsets.pipeline.lib.eventhubs.Groups;

@StageDef(
    version = 1,
    label = "Azure IoT/Event Hub Consumer",
    description = "Reads data from Azure Event Hub",
    icon = "event-hubs-multithreaded.png",
    execution = {ExecutionMode.STANDALONE},
    recordsByRef = true,
    onlineHelpRefUrl = "index.html#Origins/AzureEventHub.html#task_t14_c5q_1bb"
)
@HideConfigs({
    "consumerConfigBean.dataFormatConfig.jsonContent"
})
@ConfigGroups(Groups.class)
@GenerateResourceBundle
public class EventHubConsumerDSource extends DPushSource {

  @ConfigDefBean
  public EventHubConfigBean commonConf;

  @ConfigDefBean
  public EventHubConsumerConfigBean consumerConfigBean;

  @Override
  protected PushSource createPushSource() {
    return new EventHubConsumerSource(commonConf, consumerConfigBean);
  }
}
