package com.ans.antech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    private int idx;
    private String title;
    private String text;
    private String press;
    private String img_url1;
    private String img_url2;
    private String img_url3;
    private String url;
    private String dt;
    private String smr;
    private int view;
}
