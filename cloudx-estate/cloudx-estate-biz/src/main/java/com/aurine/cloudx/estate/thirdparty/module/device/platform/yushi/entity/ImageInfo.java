package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {

  private Long Index;

  private Long Type;

  private String Format;

  private Long Width;

  private Long Height;

  private String CaptureTime;

  private Long Size;

  private String Data;
}
