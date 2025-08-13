package com.jihunsns_back.common.response;

import java.util.List;

public record MenuItemRes(
        String id,
        String name,
        String path,
        List<String> roles,       // 허용 롤
        List<MenuItemRes> children
) {}