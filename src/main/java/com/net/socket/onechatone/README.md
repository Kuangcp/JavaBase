- 2017-4-3 17:05:59
- 使用简单的网络编程知识，Socket通信，使用协议符实现私聊
- 流程
    - 一个接口放各种情况对应的协议符
    - 一个处理Socket和用户对应的映射
    - 
    - 服务端 启动后就循环等待，通过启动的线程来判断收到的消息中的协议符是公聊还是私聊
        - 根据公聊私聊做不同的处理
    - 客户端 启动并与服务端连接后，循环接受键盘输入的字符串并发送给服务端
        - 接受服务端的消息，判断并做对应的业务处理
        
- 