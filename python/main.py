#!/usr/bin/env python
# coding: utf8
from ia.domain.model import QLearnerClean
import time
import sys

"""
Trois applications potentiels à une api :
1) Confort de python pour faire un outil de statistique de la partie
2) Outil de debug du plateau et de son état
3) Outil pour développer l'IA avec le confort de python
"""

team = int(sys.argv[1])
learner = QLearnerClean()
learner.init()
learner.learn_from_one_game(team=team)

# Second learner


# Add world to state and calculate reward

# plan = think()
# post(plan)

