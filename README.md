# Popular-Movies-AdrianaDodge  

*DetailActivity.java -line 37 replace YOUR_API_KEY_HERE for your API Movie.*  
*MainActivity.java -line 44 replace YOUR_API_KEY_HERE for your API Movie.*  


## PROJECT SPECIFICATION  
### Popular Movies, Stage 2  

![Image](https://raw.githubusercontent.com/adodge1/Popular-Movies-AdrianaDodge/master/screenshots/MainActivity.jpg)  
![Image](https://raw.githubusercontent.com/adodge1/Popular-Movies-AdrianaDodge/master/screenshots/DetailActivity.jpg)
![Image](https://raw.githubusercontent.com/adodge1/Popular-Movies-AdrianaDodge/master/screenshots/MyFavorites.jpg)
![Image](https://raw.githubusercontent.com/adodge1/Popular-Movies-AdrianaDodge/master/screenshots/Spinner-menu.jpg)
![Image](https://raw.githubusercontent.com/adodge1/Popular-Movies-AdrianaDodge/master/screenshots/Trailers&Reviews.jpg)


**Common Project Requirements**    

* App is written solely in the Java Programming Language.  
* App conforms to common standards found in the Android Nanodegree General Project Guidelines.  
* Submission must use stable release versions of all libraries, Gradle, and Android Studio. Debug/beta/canary versions are not acceptable.  
* App utilizes stable release versions of all libraries, Gradle, and Android Studio.  
* UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.  
* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.  
* UI contains a screen for displaying the details for a selected movie.  
* Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.  
* Movie Details layout contains a section for displaying trailer videos and user reviews.    

**User Interface - Function**    

* When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.  
* When a movie poster thumbnail is selected, the movie details screen is launched.  
* When a trailer is selected, app uses an Intent to launch the trailer.  
* In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.    

**Network API Implementation**    
* In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.  
* App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.  
* App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.    

**Data Persistence**    
* The titles and IDs of the user’s favorite movies are stored using Room and are updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.  
* When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.    

**Android Architecture Components**    
* Database is implemented using Room. No other persistence libraries are used.  
* Database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.  
* Database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.  
