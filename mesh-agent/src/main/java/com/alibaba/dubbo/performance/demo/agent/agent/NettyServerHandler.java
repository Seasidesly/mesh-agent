package com.alibaba.dubbo.performance.demo.agent.agent;/**
 * Created by msi- on 2018/5/16.
 */



import com.alibaba.dubbo.performance.demo.agent.agent.model.MessageRequest;
import com.alibaba.dubbo.performance.demo.agent.agent.model.MessageResponse;
import com.alibaba.dubbo.performance.demo.agent.agent.model.MyFuture;
import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClient;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcFuture;
import com.alibaba.dubbo.performance.demo.agent.registry.EtcdRegistry;
import com.alibaba.dubbo.performance.demo.agent.registry.IRegistry;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @program: TcpProject
 * @description:
 * @author: XSL
 * @create: 2018-05-16 20:29
 **/

public class NettyServerHandler extends SimpleChannelInboundHandler<MessageRequest> {
//    private Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
//    private static Executor executor = Executors.newFixedThreadPool(128);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageRequest messageRequest) throws Exception {
        MyFuture future = WaitService.executeInvoke(messageRequest);
//        logger.info("name" + channelHandlerContext.name());
//        logger.info("channel" + channelHandlerContext.channel().id());
//        logger.info("channel infos " + channelHandlerContext.channel().remoteAddress() + " " + channelHandlerContext.channel().localAddress());
        Runnable callable = () -> {
            try {
//                long time = System.currentTimeMillis();
                Integer result = JSON.parseObject((byte[]) future.get(),Integer.class);
                MessageResponse response = new MessageResponse(messageRequest.getMessageId(),result);
//                logger.info("time2 = " + (System.currentTimeMillis() - time));
                channelHandlerContext.writeAndFlush(response);
            } catch (Exception e) {
//                logger.info("error");
                channelHandlerContext.writeAndFlush(new MessageResponse(messageRequest.getMessageId(),"-1"));
                e.printStackTrace();
            }
        };
        future.addListener(callable,null);
//        executor.execute(callable);
    }
}
