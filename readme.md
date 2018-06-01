# 项目简介
---
本项目为本人毕业设计代码部分，后续会提供在线体验网站。

# 仓库构成
---
本仓库共包括三部分代码。

## Build_JAR
该部分为核心代码，需要编译成jar包供其他两个项目使用（编译好的jar包未提供，data文件夹未提供）。

### 编译环境
- 编码：UTF-8
- IDE：Eclipse Oxygen.3a
- 语言：Java
- 框架：无
- 测试：JUnit 4
- 代码规范：Google Java Format
- (平台：Mac) 

### 建议输出
项目名称“右键”  -- Export -- 选择“JAR File” -- (一切默认) -- Finish。

## Build_Desktop
该部分为JavaFX写的界面程序，需要先导入上一个项目中编译好的jar包。

### 编程环境
在上一个项目基础上，增加：

- JavaFX版本：2.0

### 打包
该项目需要使用Ant打包成应用程序。

## Build_Web
该部分为RESTFul风格的展示网站，功能与上一个项目相似，后期会发布链接供大家测试。

### 编程环境
在第一个项目基础上，增加：

- RESTful Web Services：Jersey 2.2.7

### 编译
项目名称“右键”  -- Export -- 选择“WAR File” -- (一切默认) -- Finish。