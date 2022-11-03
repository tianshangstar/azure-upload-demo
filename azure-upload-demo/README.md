# Issue description:
In Azure SDK, If running JVM with follow options
- -XX:MaxDirectMemorySize=? 
- -Xmx???

and the size provided is less than the file size which been selected to upload. 
- In v12 SDK, both these two JVM options will cause OOM error(OutOfDirectMemoryError/OutOfMemoryError)
- In v8 SDK, It works well with `-XX:MaxDirectMemorySize=?`, but throws an OutOfMemoryError with `-Xmx` options.

(In my test case, I had provide the direct memory size / heap size as 56M, and upload file size is 57M.)

# Error stack trace
### 1. If provide -XX:MaxDirectMemorySize=? (V12 SDK Thrown)
```
java.lang.IllegalStateException: io.netty.util.internal.OutOfDirectMemoryError: failed to allocate 4194304 byte(s) of direct memory (used: 54525959, max: 58720256)
at io.netty.channel.AbstractCoalescingBufferQueue.releaseAndCompleteAll(AbstractCoalescingBufferQueue.java:361)
at io.netty.channel.AbstractCoalescingBufferQueue.releaseAndFailAll(AbstractCoalescingBufferQueue.java:208)
at io.netty.handler.ssl.SslHandler.releaseAndFailAll(SslHandler.java:1909)
at io.netty.handler.ssl.SslHandler.setHandshakeFailure(SslHandler.java:1888)
at io.netty.handler.ssl.SslHandler.setHandshakeFailure(SslHandler.java:1853)
at io.netty.handler.ssl.SslHandler.flush(SslHandler.java:782)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.flush(CombinedChannelDuplexHandler.java:531)
at io.netty.channel.ChannelOutboundHandlerAdapter.flush(ChannelOutboundHandlerAdapter.java:125)
at io.netty.channel.CombinedChannelDuplexHandler.flush(CombinedChannelDuplexHandler.java:356)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
at reactor.netty.channel.MonoSendMany$SendManyInner.run(MonoSendMany.java:325)
at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:174)
at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:167)
at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:470)
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
at java.lang.Thread.run(Thread.java:750)
Caused by: io.netty.util.internal.OutOfDirectMemoryError: failed to allocate 4194304 byte(s) of direct memory (used: 54525959, max: 58720256)
at io.netty.util.internal.PlatformDependent.incrementMemoryCounter(PlatformDependent.java:845)
at io.netty.util.internal.PlatformDependent.allocateDirectNoCleaner(PlatformDependent.java:774)
at io.netty.buffer.PoolArena$DirectArena.allocateDirect(PoolArena.java:649)
at io.netty.buffer.PoolArena$DirectArena.newChunk(PoolArena.java:624)
at io.netty.buffer.PoolArena.allocateNormal(PoolArena.java:203)
at io.netty.buffer.PoolArena.tcacheAllocateSmall(PoolArena.java:173)
at io.netty.buffer.PoolArena.allocate(PoolArena.java:134)
at io.netty.buffer.PoolArena.allocate(PoolArena.java:126)
at io.netty.buffer.PooledByteBufAllocator.newDirectBuffer(PooledByteBufAllocator.java:396)
at io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:188)
at io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:179)
at io.netty.handler.ssl.SslHandler$SslEngineType$1.allocateWrapBuffer(SslHandler.java:232)
at io.netty.handler.ssl.SslHandler.allocateOutNetBuf(SslHandler.java:2266)
at io.netty.handler.ssl.SslHandler.wrap(SslHandler.java:825)
at io.netty.handler.ssl.SslHandler.wrapAndFlush(SslHandler.java:799)
at io.netty.handler.ssl.SslHandler.flush(SslHandler.java:780)
... 18 more
java.lang.IllegalStateException: io.netty.util.internal.OutOfDirectMemoryError: failed to allocate 4194304 byte(s) of direct memory (used: 54525959, max: 58720256)
at io.netty.channel.AbstractCoalescingBufferQueue.releaseAndCompleteAll(AbstractCoalescingBufferQueue.java:361)
at io.netty.channel.AbstractCoalescingBufferQueue.releaseAndFailAll(AbstractCoalescingBufferQueue.java:208)
at io.netty.handler.ssl.SslHandler.releaseAndFailAll(SslHandler.java:1909)
at io.netty.handler.ssl.SslHandler.setHandshakeFailure(SslHandler.java:1888)
at io.netty.handler.ssl.SslHandler.setHandshakeFailure(SslHandler.java:1853)
at io.netty.handler.ssl.SslHandler.flush(SslHandler.java:782)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.flush(CombinedChannelDuplexHandler.java:531)
at io.netty.channel.ChannelOutboundHandlerAdapter.flush(ChannelOutboundHandlerAdapter.java:125)
at io.netty.channel.CombinedChannelDuplexHandler.flush(CombinedChannelDuplexHandler.java:356)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush0(AbstractChannelHandlerContext.java:750)
at io.netty.channel.AbstractChannelHandlerContext.invokeFlush(AbstractChannelHandlerContext.java:742)
at io.netty.channel.AbstractChannelHandlerContext.flush(AbstractChannelHandlerContext.java:728)
at reactor.netty.channel.MonoSendMany$SendManyInner.run(MonoSendMany.java:325)
at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:174)
at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:167)
at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:470)
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:503)
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
at java.lang.Thread.run(Thread.java:750)
Suppressed: java.lang.Exception: #block terminated with an error
at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:99)
at reactor.core.publisher.Mono.block(Mono.java:1707)
at com.azure.storage.common.implementation.StorageImplUtils.blockWithOptionalTimeout(StorageImplUtils.java:191)
at com.azure.storage.blob.specialized.BlockBlobClient.uploadWithResponse(BlockBlobClient.java:486)
at com.sap.demo.fileservice.impl.AzureV12Impl.upload(AzureV12Impl.java:39)
at com.sap.demo.fileservice.Test.v12Upload(Test.java:65)
at com.sap.demo.fileservice.Test.main(Test.java:34)
Caused by: io.netty.util.internal.OutOfDirectMemoryError: failed to allocate 4194304 byte(s) of direct memory (used: 54525959, max: 58720256)
at io.netty.util.internal.PlatformDependent.incrementMemoryCounter(PlatformDependent.java:845)
at io.netty.util.internal.PlatformDependent.allocateDirectNoCleaner(PlatformDependent.java:774)
at io.netty.buffer.PoolArena$DirectArena.allocateDirect(PoolArena.java:649)
at io.netty.buffer.PoolArena$DirectArena.newChunk(PoolArena.java:624)
at io.netty.buffer.PoolArena.allocateNormal(PoolArena.java:203)
at io.netty.buffer.PoolArena.tcacheAllocateSmall(PoolArena.java:173)
at io.netty.buffer.PoolArena.allocate(PoolArena.java:134)B
at io.netty.buffer.PoolArena.allocate(PoolArena.java:126)
at io.netty.buffer.PooledByteBufAllocator.newDirectBuffer(PooledByteBufAllocator.java:396)
at io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:188)
at io.netty.buffer.AbstractByteBufAllocator.directBuffer(AbstractByteBufAllocator.java:179)
at io.netty.handler.ssl.SslHandler$SslEngineType$1.allocateWrapBuffer(SslHandler.java:232)
at io.netty.handler.ssl.SslHandler.allocateOutNetBuf(SslHandler.java:2266)
at io.netty.handler.ssl.SslHandler.wrap(SslHandler.java:825)
at io.netty.handler.ssl.SslHandler.wrapAndFlush(SslHandler.java:799)
at io.netty.handler.ssl.SslHandler.flush(SslHandler.java:780)
... 18 more
```

### 2. If provide -Xmx (below logs from V12 sdk, in V8 sdk, it will a little different.): 
```
[boundedElastic-1] ERROR reactor.core.scheduler.Schedulers - Scheduler worker in group main failed with an uncaught exception
java.lang.OutOfMemoryError: Java heap space
at java.io.BufferedInputStream.fill(BufferedInputStream.java:233)
at java.io.BufferedInputStream.read1(BufferedInputStream.java:286)
at java.io.BufferedInputStream.read(BufferedInputStream.java:345)
at com.azure.storage.common.Utility.lambda$convertStreamToByteBuffer$1(Utility.java:247)
at com.azure.storage.common.Utility$$Lambda$248/663806516.call(Unknown Source)
at reactor.core.publisher.MonoCallable.call(MonoCallable.java:92)
at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.drain(FluxConcatMap.java:410)
at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.onNext(FluxConcatMap.java:251)
at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:129)
at reactor.core.publisher.FluxRange$RangeSubscription.slowPath(FluxRange.java:156)
at reactor.core.publisher.FluxRange$RangeSubscription.request(FluxRange.java:111)
at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.request(FluxMapFuseable.java:171)
at reactor.core.publisher.FluxConcatMap$ConcatMapImmediate.onSubscribe(FluxConcatMap.java:236)
at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onSubscribe(FluxMapFuseable.java:96)
at reactor.core.publisher.FluxRange.subscribe(FluxRange.java:69)
at reactor.core.publisher.InternalFluxOperator.subscribe(InternalFluxOperator.java:62)
at reactor.core.publisher.FluxDefer.subscribe(FluxDefer.java:54)
at reactor.core.publisher.FluxSubscribeOn$SubscribeOnSubscriber.run(FluxSubscribeOn.java:194)
at reactor.core.scheduler.WorkerTask.call(WorkerTask.java:84)
at reactor.core.scheduler.WorkerTask.call(WorkerTask.java:37)
at java.util.concurrent.FutureTask.run(FutureTask.java:266)
at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$201(ScheduledThreadPoolExecutor.java:180)
at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:293)
at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
at java.lang.Thread.run(Thread.java:750)
```

# How to initialize the demo project to verify the upload functionality:
1. populate the required fields in resource/applications.properties
2. add VM options to you JVM; <br>
   Please caution that the size you provided should less than de file you want upload.
   - -XX:MaxDirectMemorySize=???
   - -Xmx???
3. Run <com.sap.demo.fileservice.Test> and choose the Azure SDK Version. <br>
   If the direct memory size or max heap size which has been set in step 2 is less than the file size you provided to upload,
   An OOM Exception will be thrown
4. ***Warning*** If the error occurred by V12 SDK, pls rerun the demo or manually trigger the GC to make sure the memory in used already be released.

## environment and JVM options
- device : MacBook Pro (16-inch, 2021)
- cpu : Apple M1 Max
- memory : 32G
- JDK : Zulu 8.62.0.19-CA-macos-aarch64 build 1.8.0_332-b09
- jvm options : No other options provided except `-Xmx` and `-XX:MaxDirectMemorySize=?`