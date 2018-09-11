# -*- coding: utf-8 -*-


import fastText as ft
import numpy as np


def get_y_pred(model, content):
    clsf = ft.load_model(model)
    result = []
    for line in content:
        tags, probs = clsf.predict(line);
        result.append(tags[0].split('__label__')[1])
    return result


def split_test_set(test_set):
    content = []
    labels = []
    with open(test_set, encoding='utf-8') as f:
        for line in f:
            tmp = line.split('__label__')
            content.append(tmp[0].strip())
            labels.append(tmp[1][:-1])
    return content, labels


def get_datas(model_path, test_set_path):
    content, y_true = split_test_set(test_set_path)
    y_pred = get_y_pred(model_path, content)
    classes = np.unique(y_true)
    return y_true, y_pred, classes
