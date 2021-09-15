package com.study.dataentryservice.model.record;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedHashSet;

@Getter
@AllArgsConstructor
public class Data {
  private LinkedHashSet<DataItem> dataItems;
  public Data() {}
}
