import functools
from functools import reduce



def string_converter():
    return ['1', '2', '3']


print(list(map(int, string_converter())))


def add_ten(number):
    return number + 10
numbers = [0, 5, 10, 15]
print(list(map(lambda x: add_ten(x), numbers)))


def temp_converter(temperatures: list):
    return list(map(lambda fahrenheit: fahrenheit / 1.8 + 32, temperatures))
temperatures = [0, 20, 37, 100]
print(temp_converter(temperatures))


def list_filter():
    return None
list_one = [1, None, 3, None, 5]
print(list(filter(list_filter(), list_one)))


def divisible_by_three(number):
    return number % 3 == 0
list_two = [1, 3, 4, 6, 9, 12]
print(list(filter(divisible_by_three, list_two)))


def positive_filter(number):
    return number > 0
list3 = [-2, -1, 0, 1, 2]
print(list(filter(positive_filter, list3)))



def dict_filter(name):
    return people[name] > 25
people = {'Dotun': 27,
          'Donald': 30,
          'Ronke': 25,
          'John': 20
          }
print(list(filter(dict_filter, people)))



def reduce_list(number, next_number):
    return number + next_number
list4 = [1, 2, 3, 4, 5]
print(reduce(reduce_list, list4))



def reduce_product(number, next_number):
    return number * next_number
list5 = [1, 2, 3, 4, 5]
print(reduce(reduce_product, list5))



def find_largest(numbers: list):
    return list(reduce(lambda current_largest, next_item: next_item if next_item > current_largest else current_largest, numbers))
print(list(find_largest))



def string_concatenate():
    return

print(list(reduce(string_concatenate, ['Hello', '', 'World', 'Something'])))