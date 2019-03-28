AOPXML实现有各种增强方式

使用AOP的xml方式来配置切面

前置
后置
最终
环绕

切面的思想很重要，编码反而不是那么重要，流程不复杂：
	切点的目标类
	提供增强的类
	配置文件的配置套路(引入AOP的dtd)
		<!-- 基本类 提供切点 -->
		<bean id="action" class="cn.spring.aop.around.ActionDao"></bean>
		<!-- 增强部分 -->
		<bean id="adder" class="cn.spring.aop.around.Round"></bean>
		<!-- 使用aop的自动提示也要配置上面的头文件声明 -->
		<aop:config>
			<!--aspect表示切面 ref 标明增强方法的类来源 -->
			<aop:aspect id="myAop" ref="adder">
				<!-- execution 是表达式（正则一样的功能）匹配的是具体的    切点 -->
				<aop:pointcut expression="execution(* cn.spring.aop.around..*(..))" id="needAdd"/>
				<!-- 织入 的过程 将增强和切入点结合 -->
				<aop:around method="roundLog" pointcut-ref="needAdd"/>
			</aop:aspect>
		</aop:config>
	