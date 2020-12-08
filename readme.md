

Автоматические проверки качества кода

Codacy Badge (app.codacy.com) -
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/7bd1b8ad1a0d48039a9d7809fd24b1b1)](https://www.codacy.com/manual/vinogor/topjava/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=vinogor/topjava&amp;utm_campaign=Badge_Grade)

Build Status (travis-ci.org) -
[![Build Status](https://travis-ci.org/vinogor/topjava.svg?branch=master)](https://travis-ci.org/vinogor/topjava)

Для корректного логирования при запуске TomCat - укажите каталог для создания логов через установку переменной окружения TOPJAVA_ROOT, например, на корень проекта
( добавить и в run и debug: TOPJAVA_ROOT=c:\Development\IdeaProjects\topjava\ )

VM options: -Dspring.profiles.active="datajpa,postgres"

mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.7.10:run
( TODO: Fatal error compiling: error: invalid target release: 15 )