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
package com.streamsets.pipeline.container;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.api.Stage.Info;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.Source;
import com.streamsets.pipeline.record.RecordImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SourcePipe extends Pipe implements Source.Context {
  private static final Set<String> EMPTY_INPUT = new HashSet<String>();

  private Source source;

  public SourcePipe(List<Info> pipelineInfo, MetricRegistry metrics, Stage.Info info, Source source,
      Set<String> outputLanes) {
    super(pipelineInfo, metrics, info, EMPTY_INPUT, outputLanes);
    Preconditions.checkNotNull(source, "source cannot be null");
    this.source = source;
  }

  @Override
  public void init() {
    try {
      source.init(getModuleInfo(), this);
    } catch (StageException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void destroy() {
    source.destroy();
  }

  @Override
  public void configure(Configuration conf) {
    super.configure(conf);
    //TODO
  }

  @Override
  protected void processBatch(PipeBatch batch) {
    Preconditions.checkNotNull(batch, "batch cannot be null");
    String newBatchId = null;
    try {
      newBatchId = source.produce(batch.getPreviousBatchId(), batch);
    } catch (StageException e) {
      e.printStackTrace();
    }
    batch.setBatchId(newBatchId);
  }

  // Source.Context

  @Override
  public Record createRecord(String sourceInfo) {
    return new RecordImpl(getModuleInfo().getInstanceName(), sourceInfo, null, null);
  }

  @Override
  public Record createRecord(String sourceInfo, byte[] raw, String rawMime) {
    return new RecordImpl(getModuleInfo().getName(), sourceInfo, raw, rawMime);
  }

  public Source getSource() {
    return source;
  }

}
