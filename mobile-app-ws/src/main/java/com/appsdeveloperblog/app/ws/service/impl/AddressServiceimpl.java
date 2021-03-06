package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.Entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.Entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.Repository.AddressRepository;
import com.appsdeveloperblog.app.ws.io.Repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceimpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String id) {
		List<AddressDto> list = new ArrayList<>();
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) {
			return list;
		}
		ModelMapper modelMapper = new ModelMapper();
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity address: addresses) {
			list.add(modelMapper.map(address, AddressDto.class));
		}
		return list;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto addressDto = null;
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		if(addressEntity == null) {
			return null;
		}
		
		addressDto = new ModelMapper().map(addressEntity,AddressDto.class);
		return addressDto;
	}

}
