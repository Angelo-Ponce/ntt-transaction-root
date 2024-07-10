package com.nttdata.service.Impl;

import com.nttdata.exception.ModelNotFoundException;
import com.nttdata.model.AccountEntity;
import com.nttdata.repository.IAccountRepository;
import com.nttdata.repository.IGenericRepository;
import com.nttdata.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends CRUDImpl<AccountEntity, Long> implements IAccountService {

    private final IAccountRepository repository;

    @Override
    protected IGenericRepository<AccountEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public void deleteLogic(Long id) {
        AccountEntity account = repository.findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        account.setStatus(Boolean.FALSE);
        repository.save(account);
    }
}
