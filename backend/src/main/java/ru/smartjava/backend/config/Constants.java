package ru.smartjava.backend.config;

import org.springframework.http.HttpMethod;

public class Constants {

    public final static String authTokenName = "auth-token";
    public final static String userNotFound = "Пользователь не найден";
    public final static String tokenNotFound = "Токен не найден";
    public final static String fileDeleteError = "Ошибка удаления файла";
    public final static String fileNotFound = "Файл не найден";
    public final static String saveFileError = "Ошибка сохранения файла";
    public final static String renameFileError = "Ошибка переименования файла";
    public final static String sourceFileAbsent = "Файл источник остутствует";
    public final static String loginField = "login";
    public final static String passwordField = "password";
    public final static String userNotAuthorized = "Пользователь не авторизован";
    public final static String loginPattern = "/login";
    public final static String loginMethod = HttpMethod.POST.name();
    public final static String originAddress = "http://localhost:8080";
    public final static String allowMethodsList = "GET, POST, PUT, DELETE, OPTIONS";
    public final static String allowHeadersList = "auth-token, authorization, content-type, xsrf-token";
    public final static Boolean allowCredentials = true;


}
