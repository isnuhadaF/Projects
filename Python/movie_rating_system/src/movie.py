
class Movie:
    def __init__(self, title):
        self.ratings = []
        self.title = title

    def set_title(self, title):
        self.title = title

    def get_title(self):
        return self.title

    def set_rating(self, rating):
        self.ratings = self.ratings.append(rating)
        
    def get_ratings(self):
        return self.ratings

    def get_average_rating(self):
        return sum(self.ratings) / len(self.ratings)