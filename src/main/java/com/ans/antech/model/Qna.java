package com.ans.antech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Qna {

    private int idx;
    private String title;
    private String text;
    private String reply;
    private String id;
    private String dt;
}
