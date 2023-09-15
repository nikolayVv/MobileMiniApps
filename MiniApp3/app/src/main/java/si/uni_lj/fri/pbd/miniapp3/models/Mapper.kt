package si.uni_lj.fri.pbd.miniapp3.models

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO

object Mapper {
    fun mapRecipeDetailsToRecipeSummaryIm(rec: RecipeDetails): RecipeSummaryIM {
        return RecipeSummaryIM(rec.strDrink!!, rec.strDrinkThumb!!, rec.idDrink!!)
    }

    fun mapRecipeDetailsDtoToRecipeDetails(isFavorite: Boolean?, dto: RecipeDetailsDTO): RecipeDetails {
        val aux = RecipeDetails()
        aux.favorite = isFavorite
        aux.idDrink = dto.idDrink
        aux.strDrink = dto.strDrink
        aux.strVideo = dto.strVideo
        aux.strCategory = dto.strCategory
        aux.strIBA = dto.strIBA
        aux.strInstructions = dto.strInstructions
        aux.strDrinkThumb = dto.strDrinkThumb
        aux.strIngredient1 = dto.strIngredient1
        aux.strIngredient2 = dto.strIngredient2
        aux.strIngredient3 = dto.strIngredient3
        aux.strIngredient4 = dto.strIngredient4
        aux.strIngredient5 = dto.strIngredient5
        aux.strIngredient6 = dto.strIngredient6
        aux.strIngredient7 = dto.strIngredient7
        aux.strIngredient8 = dto.strIngredient8
        aux.strIngredient9 = dto.strIngredient9
        aux.strIngredient10 = dto.strIngredient10
        aux.strIngredient11 = dto.strIngredient11
        aux.strIngredient12 = dto.strIngredient12
        aux.strIngredient13 = dto.strIngredient13
        aux.strIngredient14 = dto.strIngredient14
        aux.strIngredient15 = dto.strIngredient15
        aux.strMeasure1 = dto.strMeasure1
        aux.strMeasure2 = dto.strMeasure2
        aux.strMeasure3 = dto.strMeasure3
        aux.strMeasure4 = dto.strMeasure4
        aux.strMeasure5 = dto.strMeasure5
        aux.strMeasure6 = dto.strMeasure6
        aux.strMeasure7 = dto.strMeasure7
        aux.strMeasure8 = dto.strMeasure8
        aux.strMeasure9 = dto.strMeasure9
        aux.strMeasure10 = dto.strMeasure10
        aux.strMeasure11 = dto.strMeasure11
        aux.strMeasure12 = dto.strMeasure12
        aux.strMeasure13 = dto.strMeasure13
        aux.strMeasure14 = dto.strMeasure14
        aux.strMeasure15 = dto.strMeasure15
        aux.strImageSource = dto.strImageSource
        return aux
    }

//    fun mapRecipeDetailsDtoToRecipeDetailsIm(isFavorite: Boolean?, dto: RecipeDetailsDTO): RecipeDetailsIM {
//        return RecipeDetailsIM(isFavorite,
//                dto.idDrink,
//                dto.strDrink,
//                dto.strCategory,
//                dto.strIBA,
//                dto.strInstructions,
//                dto.strDrinkThumb,
//                dto.strVideo,
//                dto.strIngredient1,
//                dto.strIngredient2,
//                dto.strIngredient3,
//                dto.strIngredient4,
//                dto.strIngredient5,
//                dto.strIngredient6,
//                dto.strIngredient7,
//                dto.strIngredient8,
//                dto.strIngredient9,
//                dto.strIngredient10,
//                dto.strIngredient11,
//                dto.strIngredient12,
//                dto.strIngredient13,
//                dto.strIngredient14,
//                dto.strIngredient15,
//                dto.strMeasure1,
//                dto.strMeasure2,
//                dto.strMeasure3,
//                dto.strMeasure4,
//                dto.strMeasure5,
//                dto.strMeasure6,
//                dto.strMeasure7,
//                dto.strMeasure8,
//                dto.strMeasure9,
//                dto.strMeasure10,
//                dto.strMeasure11,
//                dto.strMeasure12,
//                dto.strMeasure13,
//                dto.strMeasure14,
//                dto.strMeasure15,
//                dto.strImageSource)
//    }

    fun mapRecipeSummaryDtoToRecipeSummaryIm(dto: RecipeSummaryDTO): RecipeSummaryIM {
        return RecipeSummaryIM(
            dto.strDrink!!,
            dto.strDrinkThumb!!,
            dto.idDrink!!
        )
    }

//    fun mapRecipeDetailsToRecipeDetailsIm(isFavorite: Boolean?, rec: RecipeDetails): RecipeDetailsIM {
//        return RecipeDetailsIM(
//                isFavorite,
//                rec.idDrink,
//                rec.strDrink,
//                rec.strCategory,
//                rec.strIBA,
//                rec.strInstructions,
//                rec.strDrinkThumb,
//                rec.strVideo,
//                rec.strIngredient1,
//                rec.strIngredient2,
//                rec.strIngredient3,
//                rec.strIngredient4,
//                rec.strIngredient5,
//                rec.strIngredient6,
//                rec.strIngredient7,
//                rec.strIngredient8,
//                rec.strIngredient9,
//                rec.strIngredient10,
//                rec.strIngredient11,
//                rec.strIngredient12,
//                rec.strIngredient13,
//                rec.strIngredient14,
//                rec.strIngredient15,
//                rec.strMeasure1,
//                rec.strMeasure2,
//                rec.strMeasure3,
//                rec.strMeasure4,
//                rec.strMeasure5,
//                rec.strMeasure6,
//                rec.strMeasure7,
//                rec.strMeasure8,
//                rec.strMeasure9,
//                rec.strMeasure10,
//                rec.strMeasure11,
//                rec.strMeasure12,
//                rec.strMeasure13,
//                rec.strMeasure14,
//                rec.strMeasure15,
//                rec.strImageSource)
//    }

}