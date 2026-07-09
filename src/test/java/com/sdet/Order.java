package com.sdet;

import java.time.LocalDate;

public record Order(String sku, int quantity, long totalPaise, String status, LocalDate orderedOn, boolean refunded){}
