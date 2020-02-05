package com.github.kuangcp.virusbroadcast.constant;

public interface Constants {

  int ORIGINAL_COUNT = 50;//初始感染数量
  float BROAD_RATE = 0.82f;//传播率
  float SHADOW_TIME = 140;//潜伏时间
  int HOSPITAL_RECEIVE_TIME = 10;//医院收治响应时间
  int BED_COUNT = 1000;//医院床位
  float INTENTION = 0.99f;//流动意向平均值
  float SAFE_DISTANCE = 2f; // 安全距离
}
