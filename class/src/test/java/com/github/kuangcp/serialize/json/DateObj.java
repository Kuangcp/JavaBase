package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Kuangcp
 * 2025-08-07 14:02
 */
@Data
public class DateObj {
    /**
     * 不带时区
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate day;

    /**
     * 带时区
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date dayZone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date dayZone2;

}
