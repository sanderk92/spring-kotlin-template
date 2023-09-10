package com.example.config

import com.example.security.user.ExtractCurrentUserAspect
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@EnableAspectJAutoProxy
@Import(value = [ExtractCurrentUserAspect::class])
class EnableAspectOrientedProgramming