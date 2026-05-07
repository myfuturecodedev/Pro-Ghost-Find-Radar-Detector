package com.futurecode.ghostfinderradardetector.utils

import com.futurecode.ghostfinderradardetector.R
import com.futurecode.ghostfinderradardetector.model.GhostModel

object GhostData {
    val ghosts = listOf(
        GhostModel(
            id = 1,
            image = R.drawable.collection_ghost_one,
            name = "Abigail",
            age = "120 years",
            dangerLevel = "Danger: Low",
            description = "Abigail is a ghost who was known for her kindness and beauty, but she was betrayed by her lover and died in a tragic way. She is now searching for her lost love and will haunt anyone who reminds her of him.",
            sounds = R.raw.female_ghost_whispers
        ),
        GhostModel(
            id = 2,
            image = R.drawable.collection_ghost_two,
            name = "Ancient Demon",
            age = "Unknown",
            dangerLevel = "Danger: High",
            description = "The Ancient Demon is a powerful and malevolent spirit that has existed for centuries. It is said to be the source of all evil in the world and is capable of possessing anyone who is weak of heart.",
            sounds = R.raw.ghost_laughter
        ),
        GhostModel(
            id = 3,
            image = R.drawable.collection_ghost_three,
            name = "Echo",
            age = "50 years",
            dangerLevel = "Danger: Mid",
            description = "Echo is a ghost who was once a beautiful singer, but she lost her voice and died of a broken heart. She is now searching for her voice and will haunt anyone who makes a sound.",
            sounds = R.raw.community_scary_scream
        ),
        GhostModel(
            id = 4,
            image = R.drawable.collection_ghost_four,
            name = "Hans",
            age = "80 years",
            dangerLevel = "Danger: Mid",
            description = "Hans is a ghost who was once a kind and gentle man, but he was betrayed by his friends and died in a tragic way. He is now searching for revenge and will haunt anyone who was involved in his death.",
            sounds = R.raw.evil_laughing
        ),
        GhostModel(
            id = 5,
            image = R.drawable.collection_ghost_five,
            name = "Krampus",
            age = "Unknown",
            dangerLevel = "Danger: Extreme",
            description = "Krampus is a powerful and malevolent spirit that is said to be the counterpart of Santa Claus. He is known for punishing children who have been naughty and is capable of carrying them away in his sack.",
            sounds = R.raw.creepy_ghost_whisper
        ),
        GhostModel(
            id = 6,
            image = R.drawable.collection_ghost_six,
            name = "Hollow Face",
            age = "100 years",
            dangerLevel = "Danger: High",
            description = "Hollow Face is a ghost who was once a beautiful woman, but she was betrayed by her husband and died in a tragic way. She is now searching for her face and will haunt anyone who looks at her.",
            sounds = R.raw.horror_lightning_strike
        ),
        GhostModel(
            id = 7,
            image = R.drawable.collection_ghost_seven,
            name = "Morax",
            age = "Unknown",
            dangerLevel = "Danger: High",
            description = "Morax is a powerful and malevolent spirit that is said to be the source of all darkness in the world. He is capable of possessing anyone who is weak of heart and is known for his cruelty.",
            sounds = R.raw.kizenkov_horror_hit_logo
        ),
        GhostModel(
            id = 8,
            image = R.drawable.collection_ghost_eight,
            name = "Selene",
            age = "60 years",
            dangerLevel = "Danger: Mid",
            description = "Selene is a ghost who was once a beautiful woman, but she was betrayed by her lover and died in a tragic way. She is now searching for her lost love and will haunt anyone who reminds her of him.",
            sounds = R.raw.lightning_magic

        ),
        GhostModel(
            id = 9,
            image = R.drawable.collection_ghost_one, // Assuming there's a 9th image or using placeholder
            name = "Azura",
            age = "Unknown",
            dangerLevel = "Danger: High",
            description = "Azura is a ghost who was once a beautiful priestess, but she was betrayed by her followers and died in a tragic way. She is now searching for her lost faith and will haunt anyone who defies her.",
            sounds = R.raw.horror_sound_ghostly
        )
    )
}