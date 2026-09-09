package com.codingShuttle.com.SpringSecurity.dto;


import com.codingShuttle.com.SpringSecurity.entity.type.BloodGroupType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor // use to populate the data
@NoArgsConstructor
@ToString
public class BloodGroupCountResponseEntity {
	
	private BloodGroupType bloodGroupType;
	
	private long count;

}
