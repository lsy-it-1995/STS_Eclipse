package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.Repository.UserRepository;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.Entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.dto.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceimpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("Record is already existed");
		}
		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30)); 
		}
		ModelMapper modelMapper = new ModelMapper();
//		BeanUtils.copyProperties(user, userEntity);
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		String publicUserId = utils.generateUserId(30);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		userEntity.setEmailVerificationToken(Utils.generateEmailVerificationToken(publicUserId));
		userEntity.setEmailVerificationStatus(false);
		UserEntity userDetail = userRepository.save(userEntity);

		UserDto userDto = modelMapper.map(userDetail, UserDto.class);
		return userDto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		// return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new
		// ArrayList());
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
				userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserDto returnVal = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UsernameNotFoundException(id);
		}
		BeanUtils.copyProperties(userEntity, returnVal);
		return returnVal;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		UserDto returnVal = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException("User with id: " + id + " not found");
		}
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		UserEntity updateUser = userRepository.save(userEntity);
		BeanUtils.copyProperties(updateUser, returnVal);
		return returnVal;
	}

	@Override
	public void deleteUser(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
		}
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		if (page > 0) {
			page = page - 1;
		}
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = userPage.getContent();
		for (UserEntity user : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnValue.add(userDto);
		}
		return returnValue;
	}

	@Override
	public boolean verifyEmail(String token) {
		boolean returnValue = false;
		UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
		if (userEntity != null) {
			boolean hasTokenExpired = Utils.hasTokenExpired(token);
			if (!hasTokenExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				returnValue = true;
			}
		}
		return returnValue;
	}

}