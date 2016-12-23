# Android-Voice-Recognition-APP

## 1. Introduction

Our project name is “Find your voice” and we want to focus on the voice of user. Most People are in favor of singing and they hope able to have beautiful songs to get attention from others. But the question is they might choose wrong songs which are difficult to perform by their voice. So how to get the right voice guidance and choosing songs are very important. As we all know, each person’s voice is unique, but you can still find the similar voice of singers whose songs may be suitable for you. We aim to design an Android App which provides singer&song suggestions by comparing user's voice feathers with famous singers. Users can clearly know which singers' voice are suitable for themselves and they can choose songs from these singers to practice.
## 2. Methodology

To extract the feather of human voice, the best method will be MFCC which is mel-frequency cepstrum coefficients. Basically, for a voice data, we first do framing and then transmit them into frequency domain(FFT). Mel filtering will be made to transmit data from regular frequency domain into Mel-frequency. Then we take the logarithm and do DCT(Discrete cosine transform), delta MFCC can be figured out by this method. Vector quantization is necessary to generate the codebook. Once we have enough MFCCs of singers, it is possible to obtain a good codebook (maybe vie LBG algorithm) which is able to determine your voice is similar to whom.
## 3. Implementation

* A. The first step is how to get the user voice file and upload to the local server. <br>
    We plan to record the voice as local file, then using FTP or UDP protocol to transfer this file to the server. <br>
* B. The second step is how to extract feathers from the user voice file. <br>
    We want to achieve this by Matlab. As it describes in part 2, we can design an efficient algorithm to get the feather voice value and compare it with singers’ in database to classify the voice. <br>
* C. The last step is how to give the suggestion back to user. <br>
    Since our server will be built by Java, so it might be easier upload or download the file to the remote mobile. Once classified the voice, singer list and song list will be generated base on it. Send the suggestions to our APP so that the user can clearly know which singers are suitable for themselves.

