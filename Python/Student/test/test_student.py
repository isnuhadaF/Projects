import unittest
from student import *


class MyTestCase(unittest.TestCase):
    def test_that_a_Student_starts_with_name_and_grade_level(self):
        student = Student("Paul Oganigwe", 9)

        self.assertEqual("Paul Oganigwe", student.name)
        self.assertEqual(9, student.grade_level)

    def test_that_a_student_can_be_introduced(self):
        student = Student("Paul Oganigwe", 9)
        expected = f"Hello, Good Day. My name is {student.name}, and i'm in grade {student.grade_level}"

        self.assertEqual(expected, student.introduce())

    def test_that_student_can_be_promoted_to_the_next_grade_level(self):
        student = Student("Paul Oganigwe", 9)
        student.promote()

        self.assertEqual(10, student.grade_level)

    def test_that_student_can_pass_or_fail_a_grade_level(self):
        student = Student("Paul Oganigwe", 9)

        expected = f"{student.name} has passed!"
        self.assertEqual(expected, student.has_passed(90))

        expected = f"{student.name} has failed!"
        self.assertEqual(expected, student.has_passed(70))

    def test_that_students_name_can_be_updated(self):
        student = Student("Paul Oganigwe", 9)
        student.update_name("Oghenechukwu Somto Paul Oganigwe")

        self.assertEqual("Oghenechukwu Somto Paul Oganigwe", student.name)


    def test_that_only_students_in_grade_twelve_can_graduate(self):
        student = Student("Paul Oganigwe", 9)
        expected = f"{student.name} is yet to graduate"

        self.assertEqual(expected, student.is_graduating())

        for promotion in range(1, 4):
            student.promote()

        expected = f"{student.name} graduated!"
        self.assertEqual(expected, student.is_graduating())


if __name__ == '__main__':
    unittest.main()
