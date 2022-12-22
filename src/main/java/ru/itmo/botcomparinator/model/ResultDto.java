package ru.itmo.botcomparinator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    String chatId;
    String caption;
    byte[] responsePhoto;
}
