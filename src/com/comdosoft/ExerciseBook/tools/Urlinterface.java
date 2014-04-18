package com.comdosoft.ExerciseBook.tools;

public interface Urlinterface {
	static final String tag = "ExerciseBook";
	static final String SHARED = "EB";
	static final double current_version = 1.0;// 应用版本号
	 static final String IP = "http://58.240.210.42:3004";
	static String[] namearr = new String[] { "听写任务", "朗读任务", "十速挑战", "选择挑战",
			"连线挑战", "完形填空", "排序挑战" };
	// static final String IP = "http://192.168.199.196:3004";
//	static final String IP = "http://116.255.202.123:3014";
	// static final String IP = "http://192.168.0.250:3004";
	// static final String IP = "http://192.168.199.196:3004";
	static String fileurl = IP + "/cjzyb.apk";
	static String filename = "cjzyb.apk";
	// 获取版本号
	static final String version = IP + "/api/current_version";
	// 获取新作业数量
	static final String NEW_HOMEWORK = IP + "/api/students/new_homework";
	// 获取班级每日任务等信息
	static final String CLASS_INFO = IP + "/api/students/get_class_info";

	// 提交作业
	static final String FINISH_QUESTION_PACKGE = IP
			+ "/api/students/finish_question_packge";
	// 记录每一题
	static final String RECORD_ANSWER_INFO = IP
			+ "/api/students/record_answer_info";
	// 加载作业题目
	static final String INTO_DAILY_TASKS = IP
			+ "/api/students/into_daily_tasks";
	// static final String HEADIMG = IP + "/user_head_img/";
	static final String NEWS_RELEASE = IP + "/api/students/news_release"; // 发表消息
	// String UPLOAD_FACE = IP + "/api/students/upload_avatar"; // 上传头像
	String get_reply_microposts = IP + "/api/students/get_reply_microposts"; // 获得
	// 子信息
	String get_class_info = IP + "/api/students/get_class_info"; // 获得 班级信息
	String reply_message = IP + "/api/students/reply_message"; // 回复 信息
	String MODIFY_PERSON_INFO = IP + "/api/students/modify_person_info"; // 修改个人信息
	String RECORD_PERSON_INFO = IP + "/api/students/record_person_info"; // 登记个人信息
	String DELETE_POSTS = IP + "/api/students/delete_posts"; // 删除主消息
	String GET_MICROPOSTS = IP + "/api/students/get_microposts"; // 分页 获取 主消息
	String get_class = IP + "/api/students/get_my_classes"; // 获得所有班级
	String Validation_into_class = IP
			+ "/api/students/validate_verification_code"; // 输入验证码，进入班级
	String get_News = IP + "/api/students/get_messages"; // 获取所有的新消息
	String DELETE_REPLY_POSTS = IP + "/api/students/delete_reply_microposts"; // 删除
	String MY_MICROPOSTS = IP + "/api/students/my_microposts"; // 分页 获取 子消息
	String getClass = IP + "/api/students/get_classmates_info";
	String delete_message = IP + "/api/students/delete_message"; // 删除消息
	String read_message = IP + "/api/students/read_message";
	String get_sysmessage = IP + "/api/students/get_sys_message"; // 获取系统消息
	String delete_sys_message = IP + "/api/students/delete_sys_message";
	// String UPLOAD_FACE = IP + "/api/students/upload_avatar"; // 上传头像
	String unfollow = IP + "/api/students/unfollow"; // 取消关注消息
	String SAVE_DICTATION = IP + "/api/students/record_answer_info"; // 保存拼写答题记录
	String QQ_LOGIN = IP + "/api/students/login"; // qq登录
	String add_concern = IP + "/api/students/add_concern"; // 关注消息
	String get_my_achievements = IP + "/api/students/get_my_archivements"; // 获得成就
	String get_knowledges_card = IP + "/api/students/get_knowledges_card"; // 获取卡包信息
	String delete_knowledges_card = IP + "/api/students/delete_knowledges_card"; // 删除卡片
	String search_tag_card = IP + "/api/students/search_tag_card";// 根据标签搜索卡片
	String knoledge_tag_relation = IP + "/api/students/knoledge_tag_relation";
	String create_card_tag = IP + "/api/students/create_card_tag";

	// 获取当天作业
	static final String get_newer_task = IP + "/api/students/get_newer_task";
	// 过往记录列表
	static final String get_more_tasks = IP + "/api/students/get_more_tasks";
	// 根据日期查询作业记录
	static final String search_tasks = IP + "/api/students/search_tasks";
	String get_follow_microposts = IP + "/api/students/get_follow_microposts"; // 关注的信息
	String get_rankings = IP + "/api/students/get_rankings"; // 获得排名
	// 提交json
	String finish_question_packge = IP + "/api/students/finish_question_packge";
}
