package com.perfumestore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Kullanıcı adı zorunlu.")
    @Size(min = 3, max = 50, message = "Kullanıcı Adı 3 ve 50 karakter aralığında olmalıdır!")
    private String username;

    @NotBlank(message = "Şifre zorunlu.")
    @Size(min = 6, max = 100, message = "Şifre 6 ve 100 karakter aralığında olmalıdır!")
    private String password;
}
