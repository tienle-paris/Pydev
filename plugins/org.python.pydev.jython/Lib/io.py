# XXX Temporary addition to Jython while we use _jyio.py in place of _io.
# This module will stand in place of the lib-python io.py. The idea is
# gradually to switch, in _jyio, between Python implementation there and
# Java implementations imported from _io as classes in Java. In the end,
# we delete this and _jyio.py, and go back to using lib-python's io.py

"""The io module provides the Python interfaces to stream handling. The
builtin open function is defined in this module.

At the top of the I/O hierarchy is the abstract base class IOBase. It
defines the basic interface to a stream. Note, however, that there is no
separation between reading and writing to streams; implementations are
allowed to throw an IOError if they do not support a given operation.

Extending IOBase is RawIOBase which deals simply with the reading and
writing of raw bytes to a stream. FileIO subclasses RawIOBase to provide
an interface to OS files.

BufferedIOBase deals with buffering on a raw byte stream (RawIOBase). Its
subclasses, BufferedWriter, BufferedReader, and BufferedRWPair buffer
streams that are readable, writable, and both respectively.
BufferedRandom provides a buffered interface to random access
streams. BytesIO is a simple stream of in-memory bytes.

Another IOBase subclass, TextIOBase, deals with the encoding and decoding
of streams into text. TextIOWrapper, which extends it, is a buffered text
interface to a buffered raw stream (`BufferedIOBase`). Finally, StringIO
is a in-memory stream for text.

Argument names are not part of the specification, and only the arguments
of open() are intended to be used as keyword arguments.

data:

DEFAULT_BUFFER_SIZE

   An int containing the default buffer size used by the module's buffered
   I/O classes. open() uses the file's blksize (as obtained by os.stat) if
   possible.
"""
# New I/O library conforming to PEP 3116.

# XXX edge cases when switching between reading/writing
# XXX need to support 1 meaning line-buffered
# XXX whenever an argument is None, use the default value
# XXX read/write ops should check readable/writable
# XXX buffered readinto should work with arbitrary buffer objects
# XXX use incremental encoder for text output, at least for UTF-16 and UTF-8-SIG
# XXX check writable, readable and seekable in appropriate places


__author__ = ("Guido van Rossum <guido@python.org>, "
              "Mike Verdone <mike.verdone@gmail.com>, "
              "Mark Russell <mark.russell@zen.co.uk>, "
              "Antoine Pitrou <solipsis@pitrou.net>, "
              "Amaury Forgeot d'Arc <amauryfa@gmail.com>, "
              "Benjamin Peterson <benjamin@python.org>")

__all__ = ["BlockingIOError", "open", "IOBase", "RawIOBase", "FileIO",
           "BytesIO", "StringIO", "BufferedIOBase",
           "BufferedReader", "BufferedWriter", "BufferedRWPair",
           "BufferedRandom", "TextIOBase", "TextIOWrapper",
           "UnsupportedOperation", "SEEK_SET", "SEEK_CUR", "SEEK_END"]


import abc

# For the time being, import everything via _jyio instead of from _io directly
import _jyio
from _jyio import (DEFAULT_BUFFER_SIZE, BlockingIOError, UnsupportedOperation,
                 open,
                 FileIO,
                 BytesIO, StringIO, BufferedReader,
                 BufferedWriter, BufferedRWPair, BufferedRandom,
                 IncrementalNewlineDecoder, TextIOWrapper)

OpenWrapper = _jyio.open # for compatibility with _pyio

# for seek()
SEEK_SET = 0
SEEK_CUR = 1
SEEK_END = 2

# Declaring ABCs in C is tricky so we do it here.
# Method descriptions and default implementations are inherited from the C
# version however.
class IOBase(_jyio._IOBase):
    __metaclass__ = abc.ABCMeta

class RawIOBase(_jyio._RawIOBase, IOBase):
    pass

class BufferedIOBase(_jyio._BufferedIOBase, IOBase):
    pass

class TextIOBase(_jyio._TextIOBase, IOBase):
    pass

RawIOBase.register(FileIO)

for klass in (BytesIO, BufferedReader, BufferedWriter, BufferedRandom,
              BufferedRWPair):
    BufferedIOBase.register(klass)

for klass in (StringIO, TextIOWrapper):
    TextIOBase.register(klass)
del klass
