package org.nikolait.assigment.telegramwebappauth.service;

import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;

public interface TelegramInitDataService {

    boolean validateInitData(String initData);

    TelegramUser parseTelegramUser(String initData);

}
