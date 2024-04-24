package com.apimgmt.gateway.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
  private String repositoryName;
  private String productName;
  private String productTitle;
  private String productShortText;
  private String productDescription;
}
