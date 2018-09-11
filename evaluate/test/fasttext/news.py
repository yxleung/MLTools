# -*- coding: utf-8 -*-


import fastText as ft
from utils import fasttext_utils
import classification.classification_visualization as cv
from sklearn.metrics import classification_report, confusion_matrix


def print_results(N, p, r):
    print("N\t" + str(N))
    print("P@{}\t{:.3f}".format(1, p))
    print("R@{}\t{:.3f}".format(1, r))


def training():
    model = ft.train_supervised(input='/Users/lyx/tmp/fasttext_demo/data/news_fasttext_train.txt', label='__label__')
    model.save_model('/Users/lyx/tmp/fasttext_demo/model/model.bin')
    # model.quantize(input='D:\\NLP\\fasttext_demo\\dataset\\news_fasttext_train.txt', retrain=True, qnorm=True,
    #                cutoff=100000)
    # model.save_model('D:/NLP/fasttext_demo/model/news_model.ftz')

    load_clsf = ft.load_model('/Users/lyx/tmp/fasttext_demo/model/model.bin')

    result = load_clsf.test('/Users/lyx/tmp/fasttext_demo/data/news_fasttext_test.txt')

    print_results(*result)


def evaluate():
    y_true, y_pred, classes = fasttext_utils.get_datas("/Users/lyx/tmp/fasttext_demo/model/model.bin",
                                                       "/Users/lyx/tmp/fasttext_demo/data/news_fasttext_test.txt")
    # cr = classification_report(labels, y_pred)
    # print(cr)
    # cv.plot_classification_report(cr)
    cm = confusion_matrix(y_true, y_pred)
    cv.plot_confusion_matrix(cm, classes)


if __name__ == '__main__':
    # training()
    evaluate()

