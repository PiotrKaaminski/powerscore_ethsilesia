import React, { FC, useState, useEffect, ChangeEvent } from 'react';
import { 
  TextField, 
  Grid,
  Typography,
  Button,
  Box,
  CircularProgress,
  Alert
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { IdentityDocumentInfo } from '../../../../types/api';
import { uploadDocumentImage, getDocumentImage } from '../../../../api/verification';

interface IdentityDocumentFormProps {
  verificationId: string;
  documentData: IdentityDocumentInfo;
}

const IdentityDocumentForm: FC<IdentityDocumentFormProps> = ({ verificationId, documentData }) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [showNotification, setShowNotification] = useState(false);

  const { data: imageBlob, isLoading: isImageLoading, isError: isImageError } = useQuery({
    queryKey: ['documentImage', verificationId],
    queryFn: async () => {
      return await getDocumentImage(verificationId);
    },
    enabled: documentData.imageUploaded,
  });

  const [serverImageUrl, setServerImageUrl] = useState<string | null>(null);

  useEffect(() => {
    if (imageBlob instanceof Blob) {
      const url = URL.createObjectURL(imageBlob);
      setServerImageUrl(url);
      return () => URL.revokeObjectURL(url);
    }
  }, [imageBlob]);

  useEffect(() => {
    return () => {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }
    };
  }, [previewUrl]);

  useEffect(() => {
    if (showNotification) {
      const timer = setTimeout(() => {
        setShowNotification(false);
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [showNotification]);

  const uploadMutation = useMutation({
    mutationFn: (file: File) => uploadDocumentImage(verificationId, file),
    onSuccess: () => {
      setShowNotification(true);
      setSelectedFile(null);
      setPreviewUrl(null);
      
      // Invalidate the verification query to refresh the document status (imageUploaded)
      queryClient.invalidateQueries({ queryKey: ['verification', verificationId] });
      // Also invalidate the document image query just in case
      queryClient.invalidateQueries({ queryKey: ['documentImage', verificationId] });
    },
    onError: () => {
      setShowNotification(true);
    }
  });

  const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }
      setSelectedFile(file);
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
    }
  };

  const handleUpload = () => {
    if (selectedFile) {
      uploadMutation.mutate(selectedFile);
    }
  };

  const displayImageUrl = documentData.imageUploaded ? serverImageUrl : previewUrl;

  return (
    <>
      <Typography variant="h4" gutterBottom align="center" sx={{ mb: 3 }}>
        {t('client.identityDocument.title')}
      </Typography>

      {showNotification && (
        <Box sx={{ mb: 3 }}>
          {uploadMutation.isSuccess ? (
            <Alert severity="success">{t('client.identityDocument.imageUploadedSuccess')}</Alert>
          ) : (
            <Alert severity="error">{uploadMutation.error?.message}</Alert>
          )}
        </Box>
      )}

      {isImageError && (
        <Box sx={{ mb: 3 }}>
          <Alert severity="error">{t('client.identityDocument.imageLoadError')}</Alert>
        </Box>
      )}

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.identityDocument.type')}
            value={documentData.type}
            disabled
          />
        </Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <TextField
            fullWidth
            label={t('client.identityDocument.number')}
            value={documentData.number}
            disabled
          />
        </Grid>

        <Grid size={{ xs: 12 }}>
          <Typography variant="h6" gutterBottom>
            {t('client.identityDocument.image')}
          </Typography>
          
          <Box sx={{ mt: 2, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
            {isImageLoading ? (
              <CircularProgress />
            ) : displayImageUrl ? (
              <Box 
                component="img" 
                src={displayImageUrl} 
                alt="Document preview" 
                sx={{ 
                  maxWidth: '100%', 
                  maxHeight: 400, 
                  borderRadius: 1, 
                  boxShadow: 3,
                  border: '1px solid',
                  borderColor: 'divider'
                }} 
              />
            ) : null}

            {!documentData.imageUploaded && (
              <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                <Button
                  variant="outlined"
                  component="label"
                >
                  {t('client.identityDocument.selectFile')}
                  <input
                    type="file"
                    hidden
                    accept="image/*"
                    onChange={handleFileChange}
                  />
                </Button>
                
                {selectedFile && (
                  <Button
                    variant="contained"
                    onClick={handleUpload}
                    disabled={uploadMutation.isPending}
                  >
                    {uploadMutation.isPending ? <CircularProgress size={24} color="inherit" /> : t('client.identityDocument.upload')}
                  </Button>
                )}
              </Box>
            )}
          </Box>
        </Grid>
      </Grid>
    </>
  );
};

export default IdentityDocumentForm;
