#!/usr/bin/env python
# -*- coding: utf-8 -*-

from matplotlib import colors, pyplot as plt
from sklearn.metrics import auc, roc_curve
import numpy as np
import itertools

ddl_heat = ['#DBDBDB', '#DCD5CC', '#DCCEBE', '#DDC8AF', '#DEC2A0', '#DEBB91',
            '#DFB583', '#DFAE74', '#E0A865', '#E1A256', '#E19B48', '#E29539']
ddlheatmap = colors.ListedColormap(ddl_heat)


def plot_classification_report(cr, title=None, cmap=ddlheatmap):
    title = title or 'Classification report'
    lines = cr.split('\n')
    classes = []
    matrix = []
    for line in lines[2:(len(lines) - 3)]:
        s = line.split()
        classes.append(s[0])
        value = [float(x) for x in s[1: len(s) - 1]]
        matrix.append(value)
    fig, ax = plt.subplots(1)
    for column in range(len(matrix) + 1):
        for row in range(len(classes)):
            # txt = matrix[row][column]
            ax.text(column, row, matrix[row][column], va='center', ha='center')
    fig = plt.imshow(matrix, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    x_tick_marks = np.arange(len(classes) + 1)
    y_tick_marks = np.arange(len(classes))
    plt.xticks(x_tick_marks, ['precision', 'recall', 'f1-score'], rotation=45)
    plt.yticks(y_tick_marks, classes)
    plt.ylabel('Classes')
    plt.xlabel('Measures')
    plt.tight_layout()
    plt.show()


def plot_confusion_matrix(cm, classes, normalize=False, title='Confusion matrix', cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        print("Normalized confusion matrix")
    else:
        print('Confusion matrix, without normalization')

    print(cm)

    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45)
    plt.yticks(tick_marks, classes)

    fmt = '.2f' if normalize else 'd'
    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, format(cm[i, j], fmt),
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.ylabel('True label')
    plt.xlabel('Predicted label')
    plt.tight_layout()
    plt.show()


def roc_compare_two(y_true_array, y_pred_array, models):
    """
    actuals = np.array([y_true_svc,y_true_knn])
    predictions = np.array([y_pred_svc,y_pred_knn])
    models = ['LinearSVC','KNeighborsClassifier']
    roc_compare_two(actuals, predictions, models)
    :param y_true_array:
    :param y_pred_array:
    :param models:
    :return:
    """
    f, (ax1, ax2) = plt.subplots(1, 2, sharey=True)
    for y_pred, m, ax in ((y_pred_array[0], models[0], ax1), (y_pred_array[1], models[1], ax2)):
        false_positive_rate, true_positive_rate, thresholds = roc_curve(y_true_array, y_pred)
        roc_auc = auc(false_positive_rate, true_positive_rate)
        ax.set_title('ROC for %s' % m)
        ax.plot(false_positive_rate, true_positive_rate, c='#2B94E9', label='AUC = %0.2f' % roc_auc)
        ax.legend(loc='lower right')
        ax.plot([0, 1], [0, 1], 'm--', c='#666666')
    plt.xlim([0, 1])
    plt.ylim([0, 1.1])
    plt.show()
