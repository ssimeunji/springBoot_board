package com.icia.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveDTO {
    @NotBlank(message = "이메일은 필수입니다.")
    private String memberWriter;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String memberPassword;
    @NotBlank(message = "이름은 필수입니다.")
    private String memberName;
}
