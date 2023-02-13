package com.spigen.asos.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportVO {
	 private String name;
	 private String content;
	 private LocalDateTime date;
}
