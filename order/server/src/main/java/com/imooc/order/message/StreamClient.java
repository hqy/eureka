package com.imooc.order.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient {

    String INPUT ="myMessage";
    String OUTPUT = "myMessage2";

    @Input(StreamClient.OUTPUT)
    SubscribableChannel input();

    @Output(StreamClient.INPUT)
    MessageChannel output();

    String INPUT2 ="handleMyMessage";
    String OUTPUT2 = "handleMyMessage2";

    @Input(StreamClient.INPUT2)
    SubscribableChannel input2();

    @Output(StreamClient.OUTPUT2)
    MessageChannel output2();

}
