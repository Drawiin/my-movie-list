package com.drawiin.mymovielist.common.movie.domain.model

data class MoviePreviewModel(
    val id: Int,
    val title: String,
    val date: String,
    val bannerUrl: String
)

val mockMoviePreviewList = listOf(
    MoviePreviewModel(
        id = 552524,
        title = "Lilo & Stitch",
        date = "2025-05-17",
        bannerUrl = "https://image.tmdb.org/t/p/w500/3bN675X0K2E5QiAZVChzB5wq90B.jpg"
    ),
    MoviePreviewModel(
        id = 950387,
        title = "A Minecraft Movie",
        date = "2025-03-31",
        bannerUrl = "https://image.tmdb.org/t/p/w500/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg"
    ),
    MoviePreviewModel(
        id = 574475,
        title = "Final Destination Bloodlines",
        date = "2025-05-14",
        bannerUrl = "https://image.tmdb.org/t/p/w500/6WxhEvFsauuACfv8HyoVX6mZKFj.jpg"
    ),
    MoviePreviewModel(
        id = 1232546,
        title = "Until Dawn",
        date = "2025-04-23",
        bannerUrl = "https://image.tmdb.org/t/p/w500/juA4IWO52Fecx8lhAsxmDgy3M3.jpg"
    ),
    MoviePreviewModel(
        id = 1197306,
        title = "A Working Man",
        date = "2025-03-26",
        bannerUrl = "https://image.tmdb.org/t/p/w500/6FRFIogh3zFnVWn7Z6zcYnIbRcX.jpg"
    ),
    MoviePreviewModel(
        id = 1257960,
        title = "Sikandar",
        date = "2025-03-29",
        bannerUrl = "https://image.tmdb.org/t/p/w500/t48miSSfe7COqgbgMyRIyPVTBoM.jpg"
    ),
    MoviePreviewModel(
        id = 575265,
        title = "Mission: Impossible - The Final Reckoning",
        date = "2025-05-17",
        bannerUrl = "https://image.tmdb.org/t/p/w500/z53D72EAOxGRqdr7KXXWp9dJiDe.jpg"
    ),
    MoviePreviewModel(
        id = 1098006,
        title = "Fountain of Youth",
        date = "2025-05-19",
        bannerUrl = "https://image.tmdb.org/t/p/w500/4iWjGghUj2uyHo2Hyw8NFBvsNGm.jpg"
    ),
    MoviePreviewModel(
        id = 1001414,
        title = "Fear Street: Prom Queen",
        date = "2025-05-23",
        bannerUrl = "https://image.tmdb.org/t/p/w500/gevScWYkF8l5i9NjFSXo8HfPNyy.jpg"
    ),
    MoviePreviewModel(
        id = 896536,
        title = "The Legend of Ochi",
        date = "2025-04-18",
        bannerUrl = "https://image.tmdb.org/t/p/w500/wVujUVvY4qvKARAslItQ4ARKqtz.jpg"
    )
    // Add more items as needed...
)
