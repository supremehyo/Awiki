package com.supremehyo.awiki.DTO

import com.supremehyo.awiki.repository.wiki.WikiContract

data class HometoEditDTO(
    var wikiDTO : WikiContract?,
    var readOrWrite : String
)
