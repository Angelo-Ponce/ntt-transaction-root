package com.nttdata.service;

import com.nttdata.model.AccountEntity;

public interface IAccountService extends ICRUD<AccountEntity, Long> {

    void deleteLogic(Long id);

}
