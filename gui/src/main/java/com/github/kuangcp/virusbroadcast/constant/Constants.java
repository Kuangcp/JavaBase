package com.github.kuangcp.virusbroadcast.constant;

public interface Constants {

  int BASE_RATE = 10000;

  int ORIGINAL_COUNT = 50;//初始感染数量
  float BROAD_RATE = 0.42f;//传播率
  float SHADOW_TIME = 14;//潜伏时间
  int HOSPITAL_RECEIVE_TIME = 10;//医院收治响应时间
  int BED_COUNT = 1500;//医院床位
  float INTENTION = 0.79f;//流动意向平均值
  float SAFE_DISTANCE = 1f; // 安全距离
  int CITY_PERSON_SCALE = 6000;

  int DEAD_RATE = 10; // 每天可能死亡率 万分比
  int CURE_RATE = 100;// 每天可能治愈率 万分比
}
