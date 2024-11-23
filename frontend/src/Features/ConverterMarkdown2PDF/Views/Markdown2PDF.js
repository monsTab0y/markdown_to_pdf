import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';

function Markdown2PDF() {
  const [loading, setLoading] = useState(false);
  const [file, setFile] = useState(null);
  const [pdfBlobUrl, setPdfBlobUrl] = useState(null);
  const [dragging, setDragging] = useState(false);
  const HEADER_SIZE = 4 * 1024; // 4KB
  const MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

  // Check if the uploaded file is a Markdown file
  const isMarkdownFile = (file) => {
    const fileExtension = file.name.split('.').pop().toLowerCase();
    return fileExtension === 'md';
  };

  // Handle uploaded file
  const handleUploadedFile = (uploadedFile) => {
    // Check if the file is too large
    if (uploadedFile.size > MAX_FILE_SIZE - HEADER_SIZE) {
      alert('File size exceeds 2MB. Please upload a smaller file.');
      return;
    }
    // Check if the file is a Markdown file
    if (uploadedFile && isMarkdownFile(uploadedFile)) {
      setFile(uploadedFile);
    } else {
      alert('Please upload a valid .md file');
    }
  };

  // Handle file selection
  const handleFileChange = (event) => {
    const uploadedFile = event.target.files[0];
    handleUploadedFile(uploadedFile);
  };

  const handleDragOver = (event) => {
    event.preventDefault();
    setDragging(true);
  };

  const handleDragLeave = () => {
    setDragging(false);
  };

  const handleDrop = (event) => {
    event.preventDefault();
    setDragging(false);
    const uploadedFile = event.dataTransfer.files[0];
    handleUploadedFile(uploadedFile);
  };

  // Handle file conversion through API MD to PDF
  const convertMarkdownToPDF = useCallback(async () => {
    if (!file) return; // Ensure file exists before uploading

    const formData = new FormData();
    formData.append('file', file);

    try {
      // Upload the file to the server
      setLoading(true);
      const response = await axios.post('api/convert', formData, {
        responseType: 'blob', // Handle binary response
      });
      setLoading(false);

      // Create a temporary URL for the Blob response
      const blobUrl = URL.createObjectURL(response.data);

      // Set the Blob URL to be used in the iframe
      setPdfBlobUrl(blobUrl);

      // Trigger download
      const downloadLink = document.createElement('a');
      downloadLink.href = blobUrl;
      downloadLink.download = `${file.name.replace('.md', '')}.pdf`;
      downloadLink.target = '_blank';
      document.body.appendChild(downloadLink);
      downloadLink.click();
      document.body.removeChild(downloadLink);

      console.log('PDF generated and ready to view');
    } catch (error) {
      console.error('Error uploading file:', error.response ? error.response.data : error.message);
    }
  }, [file]);

  // Automatically convert the uploaded file to PDF when it changes
  useEffect(() => {
    if (file) {
      convertMarkdownToPDF();
    }
  }, [file, convertMarkdownToPDF]);

  // Prevent default drag and drop behavior
  useEffect(() => {
    const handleDocumentDrop = (event) => {
      event.preventDefault();
    };

    const handleDocumentDragOver = (event) => {
      event.preventDefault();
    };

    document.addEventListener('drop', handleDocumentDrop);
    document.addEventListener('dragover', handleDocumentDragOver);

    return () => {
      document.removeEventListener('drop', handleDocumentDrop);
      document.removeEventListener('dragover', handleDocumentDragOver);
    };
  }, []);

  return (
    <div className="h-dvh flex flex-col items-center justify-center p-9 gap-5 dark:bg-stone-900">
      <h1 className="text-4xl font-black w-fit mx-auto mb-44 dark:text-stone-300">Markdown to PDF</h1>

      {/* Upload File */}
      <div
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        className="flex items-center justify-center w-full max-w-lg">
        <label htmlFor="dropzone-file" className={`flex flex-col items-center justify-center w-full h-64 border-2 border-stone-300 border-dashed rounded-lg cursor-pointer bg-stone-50 dark:hover:bg-stone-800 dark:bg-stone-700 hover:bg-stone-100 dark:border-stone-600 dark:hover:border-stone-500 ${dragging? "bg-stone-100 dark:border-stone-500 dark:bg-stone-800": ""}`}>
          <div className="flex flex-col items-center justify-center pt-5 pb-6">
            <svg className="w-8 h-8 mb-4 text-stone-500 dark:text-stone-400" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
              <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2" />
            </svg>
            <p className="mb-2 text-sm text-stone-500 dark:text-stone-400">
              <span className="font-semibold">Click to upload</span> or drag and drop
            </p>
            <p className="text-xs text-stone-500 dark:text-stone-400">file_name.md (MAX 2MB)</p>
          </div>
          <input
            id="dropzone-file"
            type="file"
            className="hidden"
            accept=".md"
            onChange={handleFileChange}
          />
        </label>
      </div>

      {/* Display Selected File */}
      <div className="min-h-8">
        <p className="mt-2 text-sm text-stone-500 dark:text-stone-400">
          {file ? `Selected file: ${file.name}` : 'No file selected'}
        </p>
      </div>

      {/* Processing file */}
      {loading && (
        <button type="button" className="flex items-center justify-center w-full max-w-lg p-3 text-white bg-blue-500 rounded-lg hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-800 ..." disabled>
          <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Processing...
        </button>
      )}

      {/* Download button */}
      {!loading && pdfBlobUrl && file && (
        <a
          href={pdfBlobUrl}
          download={`${file.name.replace('.md', '')}.pdf`}
          className="flex items-center justify-center w-full max-w-lg p-3 text-white font-bold bg-blue-500 rounded-lg hover:bg-blue-600 dark:bg-blue-700 dark:hover:bg-blue-800">
          Download PDF
        </a>
      )}
    </div>
  );
}

export default Markdown2PDF;
