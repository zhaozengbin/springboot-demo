package com.zzb.log;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestGrok {
    @Test
    public void grokTest() {
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        // 进行注册, registerDefaultPatterns()方法注册的是Grok内置的patterns
        grokCompiler.registerDefaultPatterns();
        /*
         传入自定义的pattern, 会从已注册的patterns里面进行配对, 例如: TIMESTAMP_ISO8601:timestamp1, TIMESTAMP_ISO8601在注册的
         patterns里面有对应的解析格式, 配对成功后, 会在match时按照固定的解析格式将解析结果存入map中, 此处timestamp1作为输出的key
          */
        Grok grok = grokCompiler.compile("%{TIMESTAMP_ISO8601:timestamp1}%{SPACE}%{WORD:location}"
                + ".%{WORD:level}%{SPACE}%{IP:ip}%{SPACE}%{MONTH:month}");
        String logMsg = "2018-08-23 02:56:53 Local7.Info 171.8.79.214 Aug Aug  23 02:56:53 2018 "
                + "S7506E-A %%10OSPF/6/ORIGINATE_LSA(t): OSPF TrapIDpID1.3.6.1.2.1..1.2.1.14.16.2.1.2.12ospfOriginateLsa: "
                + "Originate new LSA AreaId 0.0.0.0 LsdbType 5 LsdbLsid id 192.168.17.16Lsd LsdbRouterId Id 192.168.250.254"
                + " Rou Router er 192.168.250.254.";
        // 通过match()方法进行匹配, 对log进行解析, 按照指定的格式进行输出
        Match grokMatch = grok.match(logMsg);
        // 获取结果
        Map<String, Object> resultMap = grokMatch.capture();
        System.out.println(resultMap);
    }

    @Test
    public void grokException() {
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        // 进行注册, registerDefaultPatterns()方法注册的是Grok内置的patterns
        grokCompiler.registerDefaultPatterns();
        grokCompiler.register("MESSAGE", "[\\s\\S]*");
        /*
         传入自定义的pattern, 会从已注册的patterns里面进行配对, 例如: TIMESTAMP_ISO8601:timestamp1, TIMESTAMP_ISO8601在注册的
         patterns里面有对应的解析格式, 配对成功后, 会在match时按照固定的解析格式将解析结果存入map中, 此处timestamp1作为输出的key
          */
        Grok grok = grokCompiler.compile("%{TIMESTAMP_ISO8601:timestamp} \\[%{MESSAGE:thread}\\] %{LOGLEVEL:loglevel} %{DATA:exception_info} - %{MESSAGE:message}");
        String logMsg = "2021-02-25 10:09:50.781 [http-nio-11930-exec-163] ERROR c.d.ntiku.controller.handle.TikuExceptionHandler - 异常信息：org.apache.catalina.connector.ClientAbortException: java.io.IOException: Broken pipe\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.realWriteBytes(OutputBuffer.java:341)\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.flushByteBuffer(OutputBuffer.java:766)\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.append(OutputBuffer.java:671)\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.writeBytes(OutputBuffer.java:376)\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.write(OutputBuffer.java:354)\n" +
                "\tat org.apache.catalina.connector.CoyoteOutputStream.write(CoyoteOutputStream.java:96)\n" +
                "\tat com.fasterxml.jackson.core.json.UTF8JsonGenerator._flushBuffer(UTF8JsonGenerator.java:2085)\n" +
                "\tat com.fasterxml.jackson.core.json.UTF8JsonGenerator._writeStringSegment2(UTF8JsonGenerator.java:1400)\n" +
                "\tat com.fasterxml.jackson.core.json.UTF8JsonGenerator._writeStringSegment(UTF8JsonGenerator.java:1347)\n" +
                "\tat com.fasterxml.jackson.core.json.UTF8JsonGenerator.writeString(UTF8JsonGenerator.java:460)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.serializeContents(IndexedStringListSerializer.java:103)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.serialize(IndexedStringListSerializer.java:77)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.serialize(IndexedStringListSerializer.java:22)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:727)\n" +
                "\tat com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:719)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:155)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serializeContents(IndexedListSerializer.java:119)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:79)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:18)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:727)\n" +
                "\tat com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:719)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:155)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serializeContents(IndexedListSerializer.java:119)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:79)\n" +
                "\tat com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer.serialize(IndexedListSerializer.java:18)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:727)\n" +
                "\tat com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:719)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:155)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:727)\n" +
                "\tat com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:719)\n" +
                "\tat com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:155)\n" +
                "\tat com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:480)\n" +
                "\tat com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:319)\n" +
                "\tat com.fasterxml.jackson.databind.ObjectWriter$Prefetch.serialize(ObjectWriter.java:1396)\n" +
                "\tat com.fasterxml.jackson.databind.ObjectWriter.writeValue(ObjectWriter.java:913)\n" +
                "\tat org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.writeInternal(AbstractJackson2HttpMessageConverter.java:287)\n" +
                "\tat org.springframework.http.converter.AbstractGenericHttpMessageConverter.write(AbstractGenericHttpMessageConverter.java:103)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(AbstractMessageConverterMethodProcessor.java:290)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.handleReturnValue(RequestResponseBodyMethodProcessor.java:180)\n" +
                "\tat org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(HandlerMethodReturnValueHandlerComposite.java:82)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:122)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:892)\n" +
                "\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797)\n" +
                "\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n" +
                "\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1039)\n" +
                "\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:942)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1005)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:908)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:660)\n" +
                "\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:882)\n" +
                "\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:741)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat com.github.xiaoymin.swaggerbootstrapui.filter.SecurityBasicAuthFilter.doFilter(SecurityBasicAuthFilter.java:84)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat com.github.xiaoymin.swaggerbootstrapui.filter.ProductionSecurityFilter.doFilter(ProductionSecurityFilter.java:45)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.servlet.resource.ResourceUrlEncodingFilter.doFilter(ResourceUrlEncodingFilter.java:63)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:124)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter.doFilterInternal(HttpTraceFilter.java:88)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:96)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:92)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:93)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.filterAndRecordMetrics(WebMvcMetricsFilter.java:114)\n" +
                "\tat org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:104)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:200)\n" +
                "\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:109)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat com.alibaba.csp.sentinel.adapter.servlet.CommonFilter.doFilter(CommonFilter.java:89)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\n" +
                "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)\n" +
                "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\n" +
                "\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:490)\n" +
                "\tat org.apache.catalina.core.StandardHostValve.invoke$original$og9qT7kd(StandardHostValve.java:139)\n" +
                "\tat org.apache.catalina.core.StandardHostValve.invoke$original$og9qT7kd$accessor$Xu7EFvAd(StandardHostValve.java)\n" +
                "\tat org.apache.catalina.core.StandardHostValve$auxiliary$Aty8ADoB.call(Unknown Source)\n" +
                "\tat org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstMethodsInter.intercept(InstMethodsInter.java:93)\n" +
                "\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java)\n" +
                "\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)\n" +
                "\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\n" +
                "\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)\n" +
                "\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:408)\n" +
                "\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\n" +
                "\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:853)\n" +
                "\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1587)\n" +
                "\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\n" +
                "\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n" +
                "\tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)\n" +
                "\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)\n" +
                "\tat java.lang.Thread.run(Thread.java:748)\n" +
                "Caused by: java.io.IOException: Broken pipe\n" +
                "\tat sun.nio.ch.FileDispatcherImpl.write0(Native Method)\n" +
                "\tat sun.nio.ch.SocketDispatcher.write(SocketDispatcher.java:47)\n" +
                "\tat sun.nio.ch.IOUtil.writeFromNativeBuffer(IOUtil.java:93)\n" +
                "\tat sun.nio.ch.IOUtil.write(IOUtil.java:65)\n" +
                "\tat sun.nio.ch.SocketChannelImpl.write(SocketChannelImpl.java:471)\n" +
                "\tat org.apache.tomcat.util.net.NioChannel.write(NioChannel.java:140)\n" +
                "\tat org.apache.tomcat.util.net.NioBlockingSelector.write(NioBlockingSelector.java:101)\n" +
                "\tat org.apache.tomcat.util.net.NioSelectorPool.write(NioSelectorPool.java:152)\n" +
                "\tat org.apache.tomcat.util.net.NioEndpoint$NioSocketWrapper.doWrite(NioEndpoint.java:1261)\n" +
                "\tat org.apache.tomcat.util.net.SocketWrapperBase.doWrite(SocketWrapperBase.java:793)\n" +
                "\tat org.apache.tomcat.util.net.SocketWrapperBase.writeBlocking(SocketWrapperBase.java:563)\n" +
                "\tat org.apache.tomcat.util.net.SocketWrapperBase.write(SocketWrapperBase.java:501)\n" +
                "\tat org.apache.coyote.http11.Http11OutputBuffer$SocketOutputBuffer.doWrite(Http11OutputBuffer.java:538)\n" +
                "\tat org.apache.coyote.http11.filters.ChunkedOutputFilter.doWrite(ChunkedOutputFilter.java:112)\n" +
                "\tat org.apache.coyote.http11.Http11OutputBuffer.doWrite(Http11OutputBuffer.java:190)\n" +
                "\tat org.apache.coyote.Response.doWrite(Response.java:599)\n" +
                "\tat org.apache.catalina.connector.OutputBuffer.realWriteBytes(OutputBuffer.java:329)\n" +
                "\t... 119 more";
        ;
        // 通过match()方法进行匹配, 对log进行解析, 按照指定的格式进行输出
        Match grokMatch = grok.match(logMsg);
        // 获取结果
        Map<String, Object> resultMap = grokMatch.capture();
        resultMap.get("message").toString().split("\n\t");
        System.out.println(resultMap);
    }
}
