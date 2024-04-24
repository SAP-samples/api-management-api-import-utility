package com.apimgmt.gateway.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseData<T> {
  private T spec;
  private int status_code;
  private boolean success;
}
