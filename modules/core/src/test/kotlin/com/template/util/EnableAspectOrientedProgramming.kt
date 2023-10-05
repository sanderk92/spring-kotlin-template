package com.template.util

import com.template.security.user.ExtractCurrentUserAspect
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@EnableAspectJAutoProxy
@Import(value = [ExtractCurrentUserAspect::class])
class EnableAspectOrientedProgramming
