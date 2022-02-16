package com.appsdeveloperblog.app.ws.io.Repository;

import org.springframework.data.repository.CrudRepository;

import com.appsdeveloperblog.app.ws.io.Entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
}
