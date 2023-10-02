package ru.smartjava.backend.config;

import org.springframework.http.HttpMethod;


public class Constants {

    //Имя для сериализации названия токена
    public final static String authTokenName = "auth-token";

    //Пути для работы контроллера
    public final static String urlFilePath = "file";
    public final static String urlListPath = "list";

    //Параметры для фильтра авторизации
    public final static String loginPattern = "/login";
    public final static String loginMethod = HttpMethod.POST.name();

    //Параметры для cors фильтра
    public final static String  allowMethodsList = "GET, POST, PUT, DELETE, OPTIONS";
    public final static String allowHeadersList = "auth-token, authorization, content-type, xsrf-token";
    public final static String allowCredentials = Boolean.TRUE.toString();

    //Текстовка сообщений
    public final static String userNotFound = "Пользователь не найден";
    public final static String tokenNotFound = "Токен не найден";
    public final static String fileDeleteError = "Ошибка удаления файла";
    public final static String fileNotFound = "Файл не найден";
    public final static String saveFileError = "Ошибка сохранения файла";
    public final static String renameFileError = "Ошибка переименования файла";
    public final static String sourceFileAbsent = "Файл источник остутствует";
    public final static String userNotAuthorized = "Пользователь не авторизован";

}
